import axios from "axios";
import { BASE_URL } from "@/config/config";
import type { Task } from "@/types/types";

export const getTasks = async () => {
    try {
        const response = await axios.get(
            `${BASE_URL}/tasks`
        )
        return response.data
    } catch(error: unknown) {
        console.log(error)
    }
}

export const getTaskById = async (id: number) => {
    try {
        const response = await axios.get(
            `${BASE_URL}/tasks/${id}`
        )
        return response.data
    } catch(error: unknown) {
        console.log(error)
    }
}

export const postNewTask = async(newTaskData: Omit<Task, 'id'>) => {
    try {
        const response = await axios.post(
            `${BASE_URL}/tasks`,
            newTaskData
        )
        return response.data;
    } catch(error: unknown) {
        console.log(error)
    }
}

export const updateTaskAsComplete = async (id: number) => {
    try {
        const response = await axios.put(
            `${BASE_URL}/tasks/${id}/complete`
        )
        return response.data
    } catch(error: unknown) {
        console.log(error)
    }
}

export const updateTaskAsIncomplete = async (id: number) => {
    try {
        const response = await axios.put(
            `${BASE_URL}/tasks/${id}/incomplete`
        )
        return response.data
    } catch(error: unknown) {
        console.log(error)
    }
}

export const deleteTask = async (id: number) => {
    try {
        const response = await axios.delete(
            `${BASE_URL}/tasks/${id}/incomplete`
        )
        return response.data
    } catch(error: unknown) {
        console.log(error)
    }
}