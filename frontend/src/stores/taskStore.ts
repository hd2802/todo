import { create } from "zustand";
import axios from "axios";

interface TaskState {
    tasks: Task[]
    fetchTasks: () => Promise<void>
}

type Task = {
    id: number
    title: string
    description: string
    completed: boolean
    dueDate: string
}

export const useTaskStore = create<TaskState>((set) => ({
    tasks: [],

    fetchTasks: async(): Promise<void> => {
        try {
            const response = await axios.get(
                "http://localhost:8080/tasks"
            )
            set({ tasks: response.data })
        } catch(error: unknown) {
            console.log(error)
        }
    }
}))