import type { Task } from "@/types/types";

interface TaskProps {
    task: Task
}

export function TaskView({ task }: TaskProps) {
    return (
        <div>
            {task.title}
        </div>
    )
}