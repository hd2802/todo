export interface TaskState {
    tasks: Task[]
    fetchTasks: () => Promise<void>
}

export type Task = {
    id: number
    title: string
    description: string
    completed: boolean
    dueDate: string
}