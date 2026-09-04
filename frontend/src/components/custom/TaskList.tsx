import { TaskItem } from "@/components/custom/TaskItem";
import type { Task } from "@/types/types";
import { Card } from "@/components/ui/card";

interface TaskListProps {
    tasks: Task[]
}

export function TaskList({ tasks }: TaskListProps) {
    return (
        <div className="gap-2 text-xs">
            {tasks.length !== 0 && (
                <>
                    {tasks.map((task: Task) => {
                        return <TaskItem task={task} />
                    })}
                </>
            )}
            {tasks.length === 0 && (
                <Card className="p-2 flex justify-between flex-row items-center rounded-xs text-xs">No tasks in this list</Card>
            )}
        </div>
    )
}