import { Navigate } from "react-router";

const ProtectedRouter=({children})=>{
    const isLoggedIn=!!localStorage.getItem('token');

    if(!isLoggedIn){
        return <Navigate to="/" replace/>
    }
    return children;
}

export default ProtectedRouter;