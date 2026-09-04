import moment from "moment";
import { useEffect } from "react";
import { useTaskStore } from "@/stores/taskStore";
import { TaskList } from "@/components/custom/TaskList";

export function App() {
  const { tasks, completedTasks, notCompletedTasks, fetchTasks } = useTaskStore();

  useEffect(() => {
    void fetchTasks();
  }, [fetchTasks]);

  return (
    <div className="flex min-h-screen justify-center p-6 w-full">
      <div className="flex flex-col max-w-md min-w-0 gap-4 text-sm leading-loose w-full">
        <div className="w-full mb-4 mt-16">
          <span className="text-left text-xl block">{moment().format("dddd, MMMM Do YYYY")}</span>
        </div>

        <div className="flex flex-col gap-4 w-full">
            <span>Active:</span>
            <TaskList tasks={notCompletedTasks} />

            <span>Completed:</span>
            <TaskList tasks={completedTasks} />
        </div>
      </div>
    </div>
  );
}

export default App;