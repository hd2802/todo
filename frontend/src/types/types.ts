export interface TaskState {
    tasks: Task[]
    completedTasks: Task[]
    notCompletedTasks: Task[]
    overdueTasks: Task[]
    dueToday: Task[]
    currentTask: Task | null
    isLoading: boolean
    error: string | null
    fetchTasks: () => Promise<void>
    fetchTaskById: (id: number) => Promise<void>
    createTask: (newData: Omit<Task, 'id'>) => Promise<void>
    setTaskAsComplete: (id: number) => Promise<void>
    setTaskAsIncomplete: (id: number) => Promise<void>
    removeTask: (id: number) => Promise<void>
}

export type Task = {
    id: number
    title: string
    completed: boolean
    dueDate: string
}