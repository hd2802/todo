import { create } from "zustand";
import { getTasks } from "@/services/taskService";
import type { TaskState } from "@/types/types";

export const useTaskStore = create<TaskState>((set) => ({
    tasks: [],

    fetchTasks: async(): Promise<void> => {
        try {
            const data = await getTasks();
            
            if (data) {
                console.log(data)
                set({ tasks: data })
            }

        } catch(error: unknown) {
            console.log(error)
        }
    }
}))