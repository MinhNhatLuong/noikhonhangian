import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { orderService } from '../services/api';
import { useAuth } from '../context/AuthContext';

// MUI imports
import Container from '@mui/material/Container';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TableFooter from '@mui/material/TableFooter';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';

function Orders() {
  const [orderForm, setOrderForm] = useState({
    product: '',
    quantity: 1,
    price: '',
  });
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState('');
  const { logout } = useAuth();
  const navigate = useNavigate();

  const fetchOrders = useCallback(async () => {
    try {
      const response = await orderService.getOrders();
      if (response.result) {
        setOrders(response.result);
      }
    } catch (err) {
      if (err.response && err.response.status === 401) {
        logout();
        navigate('/login');
      }
      setError('Không thể tải danh sách đơn hàng.');
    }
  }, [logout, navigate]);

  useEffect(() => {
    fetchOrders();
  }, [fetchOrders]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setOrderForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await orderService.createOrder({
        ...orderForm,
        quantity: parseInt(orderForm.quantity),
        price: parseFloat(orderForm.price),
      });
      if (response.result) {
        setOrderForm({ product: '', quantity: 1, price: '' });
        fetchOrders(); // Reload orders after successful creation
      }
    } catch (err) {
      if (err.response && err.response.status === 401) {
        logout();
        navigate('/login');
      }
      setError('Không thể tạo đơn hàng. Vui lòng thử lại.');
    }
  };

  const calculateTotal = () => {
    return orders.reduce((total, order) => total + order.price * order.quantity, 0);
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
      <Paper sx={{ p: 3 }} elevation={3}>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography variant="h5">Quản lý đơn hàng</Typography>
          <Button color="error" variant="contained" onClick={handleLogout}>
            Đăng xuất
          </Button>
        </Box>

        {error && (
          <Typography color="error" mb={2}>
            {error}
          </Typography>
        )}

        <Box component="form" onSubmit={handleSubmit} mb={3}>
          <Typography variant="h6" gutterBottom>
            Tạo đơn hàng mới
          </Typography>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} sm={6} md={6}>
              <TextField
                label="Sản phẩm"
                name="product"
                value={orderForm.product}
                onChange={handleChange}
                fullWidth
                required
              />
            </Grid>
            <Grid item xs={6} sm={3} md={3}>
              <TextField
                label="Số lượng"
                name="quantity"
                type="number"
                inputProps={{ min: 1 }}
                value={orderForm.quantity}
                onChange={handleChange}
                fullWidth
                required
              />
            </Grid>
            <Grid item xs={6} sm={3} md={3}>
              <TextField
                label="Giá ($)"
                name="price"
                type="number"
                inputProps={{ step: '0.01', min: 0 }}
                value={orderForm.price}
                onChange={handleChange}
                fullWidth
                required
              />
            </Grid>
            <Grid item xs={12}>
              <Button type="submit" variant="contained" color="primary">
                Thêm đơn hàng
              </Button>
            </Grid>
          </Grid>
        </Box>

        <Typography variant="h6" gutterBottom>
          Danh sách đơn hàng
        </Typography>

        {orders.length === 0 ? (
          <Typography>Chưa có đơn hàng nào.</Typography>
        ) : (
          <TableContainer component={Paper} elevation={1}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Sản phẩm</TableCell>
                  <TableCell align="right">Số lượng</TableCell>
                  <TableCell align="right">Giá ($)</TableCell>
                  <TableCell align="right">Tổng ($)</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {orders.map((order) => (
                  <TableRow key={order.id}>
                    <TableCell>{order.product}</TableCell>
                    <TableCell align="right">{order.quantity}</TableCell>
                    <TableCell align="right">${order.price.toFixed(2)}</TableCell>
                    <TableCell align="right">${(order.price * order.quantity).toFixed(2)}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
              <TableFooter>
                <TableRow>
                  <TableCell colSpan={3} align="right">
                    <Typography fontWeight="bold">Tổng cộng:</Typography>
                  </TableCell>
                  <TableCell align="right">
                    <Typography fontWeight="bold">${calculateTotal().toFixed(2)}</Typography>
                  </TableCell>
                </TableRow>
              </TableFooter>
            </Table>
          </TableContainer>
        )}
      </Paper>
    </Container>
  );
}

export default Orders;