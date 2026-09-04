import { TaskItem } from "@/components/custom/TaskItem";
import type { Task } from "@/types/types";

interface TaskListProps {
    tasks: Task[]
}

export function TaskList({ tasks }: TaskListProps) {
    return (
        <div>
            {tasks.map((task: Task) => {
                return <TaskItem task={task} />
            })}
        </div>
    )
}