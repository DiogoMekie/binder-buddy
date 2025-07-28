import React, { useState, useEffect } from "react";

const App: React.FC = () => {
  const [currentView, setCurrentView] = useState<'login' | 'register' | 'dashboard'>(localStorage.getItem('jwtToken') ? 'dashboard' : 'login');

  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const [userInfo, setUserInfo] = useState<{ username: string; id: number } | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      const storedUsername = localStorage.getItem('username');
      const storedId = localStorage.getItem('userId');
      if (storedUsername && storedId) {
        setUserInfo({ username: storedUsername, id: parseInt(storedId) });
        setCurrentView('dashboard');
      } else {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('username');
        localStorage.removeItem('userId');
        setCurrentView('login');
      }
    }
  }, []);

  const showMessage = (type: 'success' | 'error', text: string) => {
    setMessage({ type, text });
    const timer = setTimeout(() => {
      setMessage(null);
      clearTimeout(timer);
    }, 5000);
  };

  const handleAuthSuccess = (token: string, username: string, id: number) => {
    localStorage.setItem('jwtToken', token);
    localStorage.setItem('username', username);
    localStorage.setItem('userId', id.toString());
    setUserInfo({ username, id });
    setCurrentView('dashboard');
    showMessage('success', `Welcome, ${username}!`);
  };

  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    localStorage.removeItem('userId');
    setUserInfo(null);
    setCurrentView('login');
    showMessage('success', 'You have been logged out.');
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col items-center justify-center p-4 font-sans">
      <div className="bg-white p-8 rounded-lg shadow-xl w-full max-w-md">
        <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">BinderBuddy</h1>
        {message && (
          <div className={`p-3 mb-4 rounded-md text-sm text-center ${message.type === 'success' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
            }`}>
            {message.text}
          </div>
        )}

        {currentView === 'login' && (
          <LoginForm
            onLoginSuccess={handleAuthSuccess}
            onSwitchToRegister={() => setCurrentView('register')}
            showMessage={showMessage}
          />
        )}

        {currentView === 'register' && (
          <RegisterForm
            onRegisterSuccess={handleAuthSuccess}
            onSwitchToLogin={() => setCurrentView('login')}
            showMessage={showMessage}
          />
        )}

        {currentView === 'dashboard' && userInfo && (
          <Dashboard userInfo={userInfo} onLogout={handleLogout} showMessage={showMessage} />
        )}

      </div>
    </div>
  );
};

interface LoginFormProps {
  onLoginSuccess: (token: string, username: string, id: number) => void;
  onSwitchToRegister: () => void;
  showMessage: (type: 'success' | 'error', text: string) => void;
}

const LoginForm: React.FC<LoginFormProps> = ({ onLoginSuccess, onSwitchToRegister, showMessage }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      const data = await response.json();

      if (response.ok) {
        if (data.token) {
          onLoginSuccess(data.token, data.username, data.id);
        } else {
          showMessage('error', 'Login successful but no token received.');
        }
      } else {
        showMessage('error', data.message || 'Login failed. Please check your credentials.');
      }
    } catch (error) {
      console.error('Login error:', error);
      showMessage('error', 'Network error or server unreachable.');
    } finally {
      setLoading(false);
    }
  };
  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <h2 className="text-2xl font-semibold text-gray-700 text-center mb-4">Login</h2>
      <div>
        <label htmlFor="username" className="block text-sm font-medium text-gray-700">Username</label>
        <input
          type="text"
          id="username"
          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          autoComplete="username"
        />
      </div>
      <div>
        <label htmlFor="password" className="block text-sm font-medium text-gray-700">Password</label>
        <input
          type="password"
          id="password"
          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          autoComplete="current-password"
        />
      </div>
      <button
        type="submit"
        className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition duration-150 ease-in-out disabled:opacity-50"
        disabled={loading}
      >
        {loading ? 'Logging In...' : 'Login'}
      </button>
      <p className="text-center text-sm text-gray-600">
        Don't have an account?{' '}
        <button type="button" onClick={onSwitchToRegister} className="font-medium text-blue-600 hover:text-blue-500 focus:outline-none">
          Register here
        </button>
      </p>
    </form>
  );
};

interface RegisterFormProps {
  onRegisterSuccess: (token: string, username: string, id: number) => void;
  onSwitchToLogin: () => void;
  showMessage: (type: 'success' | 'error', text: string) => void;
}

const RegisterForm: React.FC<RegisterFormProps> = ({ onRegisterSuccess, onSwitchToLogin, showMessage }) => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, email, password }),
      });

      const data = await response.json();

      if (response.ok) {
        onRegisterSuccess(data.token, data.username, data.id);
        showMessage('success', data.message || 'Registration successful! Please login.');
        onSwitchToLogin();
      } else {
        showMessage('error', data.message || 'Registration failed.');
      }
    } catch (error) {
      console.error('Registration error:', error);
      showMessage('error', 'Network error or server unreachable.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <h2 className="text-2xl font-semibold text-gray-700 text-center mb-4">Register</h2>
      <div>
        <label htmlFor="regUsername" className="block text-sm font-medium text-gray-700">Username</label>
        <input
          type="text"
          id="regUsername"
          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          autoComplete="new-username"
        />
      </div>
      <div>
        <label htmlFor="regEmail" className="block text-sm font-medium text-gray-700">Email</label>
        <input
          type="email"
          id="regEmail"
          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          autoComplete="email"
        />
      </div>
      <div>
        <label htmlFor="regPassword" className="block text-sm font-medium text-gray-700">Password</label>
        <input
          type="password"
          id="regPassword"
          className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          autoComplete="new-password"
        />
      </div>
      <button
        type="submit"
        className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition duration-150 ease-in-out disabled:opacity-50"
        disabled={loading}
      >
        {loading ? 'Registering...' : 'Register'}
      </button>
      <p className="text-center text-sm text-gray-600">
        Already have an account?{' '}
        <button type="button" onClick={onSwitchToLogin} className="font-medium text-blue-600 hover:text-blue-500 focus:outline-none">
          Login here
        </button>
      </p>
    </form>
  );
};

interface DashboardProps {
  userInfo: { username: string; id: number };
  onLogout: () => void;
  showMessage: (type: 'success' | 'error', text: string) => void;
}

const Dashboard: React.FC<DashboardProps> = ({ userInfo, onLogout, showMessage }) => {
  const [backendMessage, setBackendMessage] = useState('');
  const [loadingHello, setLoadingHello] = useState(false);

  // Function to fetch a protected message from the backend
  const fetchProtectedMessage = async () => {
    setLoadingHello(true);
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      showMessage('error', 'No token found. Please log in.');
      setLoadingHello(false);
      return;
    }

    try {
      const response = await fetch('/api/hello', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const text = await response.text(); 
        setBackendMessage(text);
        showMessage('success', 'Protected message fetched!');
      } else {
        const errorText = await response.text(); 
        showMessage('error', `Failed to fetch protected message: ${errorText}`);
        setBackendMessage(`Error: ${errorText}`);
      }
    } catch (error) {
      console.error('Fetch protected message error:', error);
      showMessage('error', 'Network error while fetching protected message.');
      setBackendMessage('Network error.');
    } finally {
      setLoadingHello(false);
    }
  };

  return (
    <div className="space-y-6 text-center">
      <h2 className="text-2xl font-semibold text-gray-700 mb-4">Welcome to your Dashboard!</h2>
      <p className="text-gray-600">Hello, <span className="font-bold text-blue-600">{userInfo.username}</span> (ID: {userInfo.id})</p>

      <button
        onClick={fetchProtectedMessage}
        className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-purple-600 hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500 transition duration-150 ease-in-out disabled:opacity-50"
        disabled={loadingHello}
      >
        {loadingHello ? 'Fetching...' : 'Fetch Protected Message'}
      </button>

      {backendMessage && (
        <p className="mt-4 p-3 bg-gray-100 rounded-md text-gray-800 break-words">{backendMessage}</p>
      )}

      <button
        onClick={onLogout}
        className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition duration-150 ease-in-out mt-4"
      >
        Logout
      </button>
    </div>
  );
};

export default App;