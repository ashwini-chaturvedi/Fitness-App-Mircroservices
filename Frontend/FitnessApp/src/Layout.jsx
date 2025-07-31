import { Outlet } from 'react-router'
import {Header,Footer} from './components/allComponents'

const Layout = () => {

    
    return (
        <>
            <Header />
            <Outlet />
            <Footer />
        </>
    )
}

export default Layout