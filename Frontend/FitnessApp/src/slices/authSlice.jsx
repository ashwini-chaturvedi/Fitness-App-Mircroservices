import { createSlice } from "@reduxjs/toolkit";

const initialState={
    user:JSON.parse(localStorage.getItem('user')) || null,
    token:localStorage.getItem('token') || null,
    userId:localStorage.getItem('userId') || null
}

const authSlice=createSlice({
    name:'auth',
    initialState,
    reducers:{
        setCredentials:(state,action)=>{
            state.user=action.payload.user
            state.token=action.payload.token
            state.userId=action.payload.user.sub

            localStorage.setItem('token',action.payload.token)
            localStorage.setItem('user',JSON.stringify(action.payload.user))
            localStorage.setItem('userId',action.payload.user.sub)
        },
        logout:(state)=>{
            state.user=null
            state.token=null
            state.userId=null

            localStorage.removeItemItem('token')
            localStorage.removeItemItem('user')
            localStorage.removeItemItem('userId')
        }
    }
})

export const {logout,setCredentials}=authSlice.actions //Give me the actions login, logout, and setCredentials from authSlice, and make them available for other files/modules to use.
export default authSlice.reducer //Exports the reducer function so it can be included in the Redux store