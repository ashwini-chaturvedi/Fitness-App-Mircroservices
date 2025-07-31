import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router'
import { ActivityForm } from '../allComponents'

const Dashboard = () => {
    const [userData, setUserData] = useState({
        emailId: "",
        userName: "",
        firstName: "",
        lastName: "",
        phoneNo: "",
        DOB: ""
    })

    const keycloakId = localStorage.getItem('userId')
    const token = localStorage.getItem('token')
    const [activities, setActivities] = useState([])
    const navigate = useNavigate()

    const backendUrl = import.meta.env.VITE_BACKEND_API

    useEffect(() => {
        const fetchUserData = async () => {

            try {
                const retrievedData = await fetch(`${backendUrl}/user/keycloak/${keycloakId}`, {
                    method: "GET",
                    headers: {
                        "X-User-ID": keycloakId,
                        "Authorization": `Bearer ${token}`
                    },
                    mode: "cors"
                })

                if (retrievedData.ok) {
                    const data = await retrievedData.json();



                    setUserData(data)
                }
            } catch (error) {
                console.log(error)
            }
        }
        fetchUserData()
    }, [backendUrl, keycloakId, token])

    useEffect(() => {
        const fetchPreviousActivity = async () => {
            try {
                const retrievedActivity = await fetch(`${backendUrl}/activity/user/${keycloakId}`, {
                    method: "GET",
                    headers: {
                        "X-User-ID": keycloakId,
                        "Authorization": `Bearer ${token}`
                    },
                    mode: "cors",
                })

                if (retrievedActivity.ok) {
                    const activitiesData = await retrievedActivity.json()
                    console.log("Data:", activitiesData)

                    setActivities(activitiesData)
                }


            } catch (error) {
                console.log(error)
            }
        }
        fetchPreviousActivity();
    }, [activities.length, backendUrl, keycloakId, token])




    return (
        <>
            <div className='flex m-5'>
                <div className='  rounded-2xl p-5 w-7/12 shadow-2xl'>
                    <div className='bg-slate-200 text-center rounded-2xl h-40 flex justify-between items-center '>
                        <div className='m-4 text-3xl'>Profile</div>
                        
                    </div>

                    <div className=' rounded-2xl h-auto'>
                        <div className=' p-2'>
                            User Name:{userData.userName ? userData.userName : "Unknown"}
                        </div>
                        <div className=' p-2'>
                            Email Id:{userData.emailId}
                        </div>
                        <div className=' p-2'>
                            Full Name:{userData.firstName} {userData.lastName}
                        </div>

                        <div className='  p-2'>
                            Mobile No.:{userData.phoneNo}
                        </div>
                        <div className=' p-2'>
                            Date of Birth:{userData.DOB}
                        </div>
                    </div>

                </div>
                <div className='m-4 rounded-2xl p-5 w-11/12 shadow-2xl'>
                     <div className='bg-slate-200 text-center rounded-2xl h-40 flex justify-between items-center '>
                        <div className='m-4 text-3xl'>Previous Activities</div>
                        <button className='m-4 bg-green-400 rounded-2xl p-4' onClick={() => navigate('/activityForm')}>Add Activity</button>
                        
                    </div>
                    



                    <div className='flex flex-wrap m-4 gap-10 justify-around'>
                        {activities.map((activity, index) => {
                            return (

                                <div className='shadow-2xl bg-yellow-200 rounded-3xl w-56 p-4' key={index}>
                                    <div className='mx-10 font-bold text-2xl'>
                                        {activity.activityType}

                                    </div>
                                    <div className='flex justify-between'>
                                        Calories <span>{activity.caloriesBurned} (cal)</span> 

                                    </div>
                                    <div className='flex justify-between'>
                                        Duration <span className='text-blue-500 text-lg'>{activity.duration} (min)</span>
                                    </div>
                                    <div className='flex justify-end'>
                                        <button className=' bg-blue-100 rounded-2xl p-2' onClick={() => navigate(`/activity/${activity.id}`)}>More Details </button>
                                    </div>



                                </div>



                            )
                        })}
                    </div>


                </div>
            </div>


        </>


    )
}

export default Dashboard