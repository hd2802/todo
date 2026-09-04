import { TaskView } from "@/components/custom/TaskView";
import type { Task } from "@/types/types";

interface TaskListProps {
    tasks: Task[]
    completedTasks: Task[]
    notCompletedTasks: Task[]
}

export function TaskList({ tasks, completedTasks, notCompletedTasks }: TaskListProps) {
    return (
        <div className="w-full max-w-md min-w-126 flex justify-center flex-col items-center">
            {notCompletedTasks.map((task) => {
                return <TaskView key={task.id} task={task} />
            })}
        </div>
    )
}