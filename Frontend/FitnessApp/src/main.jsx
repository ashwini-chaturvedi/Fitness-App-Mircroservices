import { createRoot } from 'react-dom/client'
import './index.css'
import store from './store/store'
import { Provider } from 'react-redux'
import { RouterProvider, createBrowserRouter, createRoutesFromElements, Route } from 'react-router-dom'
import ProtectedRouter from './ProtectedRouter.jsx'
import { Home,Activity,Dashboard,ActivityForm,ActivityDetail } from './components/allComponents.js'
import Layout from './Layout.jsx'
import { AuthContext, AuthProvider } from 'react-oauth2-code-pkce'
import { authConfig } from './auth/AuthConfig.jsx'



// Create the router
const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path='/' element={<Layout />} >

      <Route path='/' element={<Home />} />
      <Route path='/activity/:id' element={<Activity />} />
      <Route path='/dashboard' element={<Dashboard />} />
      <Route path='/activityForm' element={<ActivityForm />} />
    </Route>

  )
)

// Render the app
createRoot(document.getElementById('root')).render(
  <AuthProvider authConfig={authConfig}>
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>
  </AuthProvider>
)
