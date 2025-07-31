import { Box, Button, dividerClasses } from "@mui/material";
import { useContext, useEffect, useState } from 'react'
import { AuthContext } from 'react-oauth2-code-pkce'
import { useDispatch } from 'react-redux'
import { setCredentials } from "../../slices/authSlice";
import { Navigate, useNavigate } from "react-router";
import { ActivityForm, Dashboard } from "../allComponents";

const Home = () => {
    const { token, tokenData, logIn, logOut, isAuthenticated } = useContext(AuthContext)

    const dispatch = useDispatch();
    const [authReady, setAuthReady] = useState(false)

    useEffect(() => {
        if (token) {
            dispatch(setCredentials({ token, user: tokenData }))
            setAuthReady(true)
        }
    }, [dispatch, token, tokenData])

    return (
        <>

            {!token ? (                           

                <Button variant="contained" onClick={() => { logIn() }}>Submit</Button>
            ) : (
                <>
                    {/* <ActivityForm onActivityAdded={() => window.location.reload} /> */}
                    <Dashboard />
                </>

            )}

        </>
    )

}
export default Home