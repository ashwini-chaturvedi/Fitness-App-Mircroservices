import React from 'react'
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router'

const ActivityForm = ({ onActivityAdded }) => {
    const { register, handleSubmit, watch, formState: { error }, } = useForm()
    const keycloakId = localStorage.getItem('userId')
    const token = localStorage.getItem('token')
    const navigate = useNavigate()

    const backendUrl = import.meta.env.VITE_BACKEND_API


    const onSubmit = async (activityData) => {
        activityData = { ...activityData, keycloakId }
        console.log("ActivityData", activityData)
        try {
            const retrievedData = await fetch(`${backendUrl}/activity/create`, {
                method: "POST",
                mode: "cors",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                    "X-User-ID": keycloakId
                },
                body: JSON.stringify(activityData) 
            });
            console.log(retrievedData)

            if (retrievedData.ok) {
                const data = await retrievedData.json();
                console.log(data)

                navigate("/dashboard");

            }
            onActivityAdded()
        } catch (error) {
            console.log(error)
        }
    }
    return (
        <>
            <div className='bg-yellow-300 mx-80 w-auto p-10 rounded-2xl flex'>

                <div className='text-center my-32'>
                    <h1 className='text-5xl font-bold'>Activity Form</h1>
                </div>
                <form onSubmit={handleSubmit(onSubmit)} className=' rounded-lg mx-10 shadow-xl w-full '>

                    <div className='p-14 bg-slate-100 rounded-3xl '>

                        <div className='text-xl mb-4 flex justify-between '>
                            <div className=' ' >Activity Type:</div>
                            <select {...register("activityType")} className='w-48 border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400 '>
                                <option value="">Select</option>
                                <option value="RUNNING">Running</option>
                                <option value="WALKING">Walking</option>
                                <option value="CYCLING">Cycling</option>
                            </select>
                        </div>
                        <div className='text-xl mb-4 flex justify-between'>
                            <div className=''>
                                Duration
                            </div>
                            <input type='number' {...register("duration")} className='border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400 ' />
                        </div>

                        <div className='text-xl mb-4 flex justify-between'>
                            <label>Calories Burned:</label>
                            <input type='number' {...register("caloriesBurned")} className='border border-gray-300 p-2 rounded-md focus:outline-none focus:ring-2 focus:ring-green-400 ' />
                        </div>

                        <button type="submit" className='bg-green-500 w-56 text-xl mb-5 mx-32 p-3 rounded-lg'> Add an Activity </button>
                    </div>

                </form>

            </div>

        </>

    )
}

export default ActivityForm