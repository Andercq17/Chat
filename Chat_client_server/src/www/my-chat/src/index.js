import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import Home from './pages/Home';
import { BrowserRouter as Router, Route, Routes as Switch  } from 'react-router-dom';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Router>
      <Switch>
        <Route path='/' element={<App/>} />
        <Route path='/home' element={<Home/>} />
      </Switch>

    </Router>
  </React.StrictMode>
);

reportWebVitals();
