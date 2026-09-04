import { create } from "zustand";
import { 
    getTasks,
    getTaskById,
    postNewTask,
    updateTaskAsComplete,
    updateTaskAsIncomplete,
    deleteTask
} from "@/services/taskService";
import type { Task, TaskState } from "@/types/types";

export const useTaskStore = create<TaskState>((set) => ({
    tasks: [],
    completedTasks: [],
    notCompletedTasks: [],
    currentTask: null,
    isLoading: false,
    error: null, 

    fetchTasks: async(): Promise<void> => {
        set({ isLoading: true })
        try {
            const data = await getTasks();
            
            if (data) {
                set({ 
                    tasks: data, 
                    completedTasks: data.filter((task: Task) => (task.completed === true)), 
                    notCompletedTasks: data.filter((task: Task) => (task.completed === false)), 
                    isLoading: true 
                })
            }

        } catch(e: any) {
            set({ error: e.message || "Failed to fetch tasks", isLoading: false })
        }
    },

    fetchTaskById: async(id: number): Promise<void> => {
        set({ isLoading: true })
        try {
            const data = await getTaskById(id)

            if(data) {
                set({ currentTask: data, isLoading: false})
            }
        } catch(e: any) {
            set({ error: e.message || `Failed to fetch task with id ${id}`, isLoading: false })
        }
    },

    createTask: async (newTaskData: Omit<Task, 'id'>): Promise<void> => {
        set({ isLoading: true })
        try {
            const createdTask = await postNewTask(newTaskData);
            if (createdTask) {
                set((state) => ({
                    tasks: [...state.tasks, createdTask],
                    }));
                }
                set({ isLoading: false })
            } catch (e: any) {
                set({ error: e.message || `Failed to create task`, isLoading: false })
            }
    },

    setTaskAsComplete: async(id: number): Promise<void> => {
        set({ isLoading: true })
        try {
            const updatedTask = await updateTaskAsComplete(id);
            if (updatedTask) {
                console.log(`Task ${id} marked complete:`, updatedTask);
                set((state) => ({
                    tasks: state.tasks.map((task) =>
                        task.id === id ? { ...task, completed: true } : task
                    ),
                }));
                set((state) => ({
                    completedTasks: [...state.completedTasks, updatedTask],
                }));
                set((state) => ({
                    notCompletedTasks: state.notCompletedTasks.filter((task: Task) => (task.id !== updatedTask.id))
                }))
            }
            set({ isLoading: false })
        } catch(e: any) {
            set({ error: e.message || `Failed to update task with id ${id}`, isLoading: false })
        }
    },

    setTaskAsIncomplete: async(id: number): Promise<void> => {
        try {
            const updatedTask = await updateTaskAsIncomplete(id);
            if (updatedTask) {
                console.log(`Task ${id} marked incomplete:`, updatedTask);
                set((state) => ({
                    tasks: state.tasks.map((task) =>
                        task.id === id ? { ...task, completed: false } : task
                    ),
                }));
                set((state) => ({
                    notCompletedTasks: [...state.notCompletedTasks, updatedTask],
                }));
                set((state) => ({
                    completedTasks: state.completedTasks.filter((task: Task) => (task.id !== updatedTask.id))
                }))
            }
            set({ isLoading: false })
        } catch(e: any) {
            set({ error: e.message || `Failed to update task with id ${id}`, isLoading: false })
        }
    },

    removeTask: async(id: number): Promise<void> => {
        try {
            const deletedTask = await deleteTask(id)
            if(deletedTask) {
                set((state) => ({
                    tasks: state.tasks.filter((task) => 
                        task.id !== deletedTask.id
                    )
                }))
            }
            set({ isLoading: false })
        } catch(e: any) {
            set({ error: e.message || `Failed to delete task with id ${id}`, isLoading: false })
        }
    }

}))