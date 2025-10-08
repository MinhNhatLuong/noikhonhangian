import { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { authService } from '../services/api';
import { useAuth } from '../context/AuthContext';

// MUI
import Container from '@mui/material/Container';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Link from '@mui/material/Link';

function Login() {
  const [credentials, setCredentials] = useState({
    username: '',
    password: '',
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const response = await authService.login(
        credentials.username,
        credentials.password
      );
      console.log('Login response in component:', response);
      
      if (response.data && response.data.result) {
        const { accessToken, refreshToken } = response.data.result;
        // Lưu cả access token và refresh token
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        
        login({ username: credentials.username }, accessToken);
        navigate('/orders');
      } else {
        setError('Invalid response format');
        console.error('Unexpected response format:', response);
      }
    } catch (err) {
      setError(err.response?.data?.error || 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.');
      console.error('Login error full details:', err);
    }
  };

  return (
    <Container maxWidth="xs" sx={{ mt: 8 }}>
      <Paper sx={{ p: 3 }} elevation={3}>
        <Typography variant="h5" align="center" gutterBottom>
          Đăng nhập
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
            value={credentials.username}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Mật khẩu"
            name="password"
            type="password"
            value={credentials.password}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>
            Đăng nhập
          </Button>
        </Box>

        <Box textAlign="center" sx={{ mt: 2 }}>
          <Link component={RouterLink} to="/register">
            Chưa có tài khoản? Đăng ký ngay
          </Link>
        </Box>
      </Paper>
    </Container>
  );
}

export default Login;