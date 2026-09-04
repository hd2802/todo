import { useEffect } from "react";
import { TaskList } from "@/components/custom/TaskList";
import { useTaskStore } from "@/stores/taskStore";

export function App() {
    const { tasks, completedTasks, notCompletedTasks, fetchTasks } = useTaskStore()

    useEffect(() => {
        void fetchTasks()
    }, [fetchTasks]);

    console.log(tasks);

    return (
        <div className="flex min-h-screen justify-center p-6 w-full">
            <div className="flex max-w-md min-w-0 flex-col gap-4 text-sm leading-loose">
                Total tasks to complete: {notCompletedTasks.length}
                <div>
                    <TaskList tasks={tasks} completedTasks={completedTasks} notCompletedTasks={notCompletedTasks}/>
                </div>
                Tasks completed: {completedTasks.length}
            </div>
        </div>
    )
}

export default App;