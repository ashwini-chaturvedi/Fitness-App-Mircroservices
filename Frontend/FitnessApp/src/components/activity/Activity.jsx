import React, { useState,useEffect } from 'react'
import { useParams } from 'react-router-dom'
import ActivityForm from './ActivityForm'
import ActivityDetail from './ActivityDetail'

const Activity = () => {
    const {id}=useParams();

    const [detail,setDetail]=useState({
        "activityType":"",
        "recommendation":"",
        "improvements":[],
        "suggestions":[],
        "safetyMeasures":[],
        "createdAt":""
    })
    const keycloakId = localStorage.getItem('userId')
    const token = localStorage.getItem('token')
    const backendUrl = import.meta.env.VITE_BACKEND_API

    useEffect(() => {
            const fetchRecommendation = async () => {
                try {
                    const retrievedActivity = await fetch(`${backendUrl}/ai/activity/${id}`, {
                        method: "GET",
                        headers: {
                            "X-User-ID": keycloakId,
                            "Authorization": `Bearer ${token}`
                        },
                        mode: "cors",
                    })
    
                    if (retrievedActivity.ok) {
                        const activitiesData = await retrievedActivity.json()
    
                        setDetail(activitiesData)
                    }
    
    
                } catch (error) {
                    console.log(error)
                }
            }
            fetchRecommendation();
        }, [backendUrl, id, keycloakId, token])

    return (
        <>
            <div>
                <div>{detail.activityType}</div>
                <div>{detail.recommendation}</div>
            </div>

        </>

    )
}

export default Activity