import { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { authService } from '../services/api';

// MUI
import Container from '@mui/material/Container';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Link from '@mui/material/Link';

function Register() {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    fullName: '',
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const response = await authService.register(formData);
      if (response.result) {
        alert('Đăng ký thành công! Vui lòng đăng nhập.');
        navigate('/login');
      } else if (response.error) {
        setError(response.error || 'Đăng ký thất bại. Vui lòng thử lại.');
      }
    } catch (err) {
      setError(err.response?.data?.error || 'Đăng ký thất bại. Vui lòng thử lại.');
    }
  };

  return (
    <Container maxWidth="xs" sx={{ mt: 8 }}>
      <Paper sx={{ p: 3 }} elevation={3}>
        <Typography variant="h5" align="center" gutterBottom>
          Đăng ký
        </Typography>

        {error && (
          <Typography color="error" align="center" sx={{ mb: 2 }}>
            {error}
          </Typography>
        )}

        <Box component="form" onSubmit={handleSubmit} noValidate>
          <TextField
            label="Tên đăng nhập"
            name="username"
            value={formData.username}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Mật khẩu"
            name="password"
            type="password"
            value={formData.password}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Họ và tên"
            name="fullName"
            value={formData.fullName}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>
            Đăng ký
          </Button>
        </Box>

        <Box textAlign="center" sx={{ mt: 2 }}>
          <Link component={RouterLink} to="/login">
            Đã có tài khoản? Đăng nhập
          </Link>
        </Box>
      </Paper>
    </Container>
  );
}

export default Register;