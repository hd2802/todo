import { useState } from "react";
import { useTaskStore } from "@/stores/taskStore";
import type { Task } from "@/types/types";
import { Card } from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox"

interface TaskItemProps {
    task: Task
}

export function TaskItem({ task }: TaskItemProps) {
    const { setTaskAsComplete, setTaskAsIncomplete } = useTaskStore();

    const [checked, setChecked] = useState(task.completed)

    const handleCheck = async () => {
        if(checked) {
            await setTaskAsIncomplete(task.id);
        } else {
            await setTaskAsComplete(task.id);
        }
        setChecked(!checked)
        return 
    }

    return (
        <Card className="p-1 flex justify-between flex-row items-center rounded-xs mt-1 mb-1 text-xs">
            <div className="flex flex-row justify-start items-center">
                <Checkbox 
                    checked={checked}
                    onCheckedChange={handleCheck}
                    className="w-4 h-4"
                />
                <div>
                    <span className="ml-2">{task.title}</span>
                    <p></p>
                </div>
            </div>
        </Card>
    )
}   