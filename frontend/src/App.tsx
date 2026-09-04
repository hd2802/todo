import moment from "moment";
import { useEffect } from "react";
import { useTaskStore } from "@/stores/taskStore";
import { AddTask } from "@/components/custom/AddTask";
import { TaskList } from "@/components/custom/TaskList";

export function App() {
  const { tasks, completedTasks, notCompletedTasks, dueToday, overdueTasks, fetchTasks } = useTaskStore();

  useEffect(() => {
    void fetchTasks();
  }, [fetchTasks]);

  return (
    <div className="flex min-h-screen justify-center p-4 w-full">
      <div className="flex flex-col max-w-256 min-w-128 gap-2 text-xs leading-loose w-full">
        <div className="w-full mb-2 mt-8">
          <span className="text-left text-lg block">{moment().format("dddd, MMMM Do YYYY")}</span>
        </div>

        <div className="flex flex-col w-full">
            <AddTask />

            <span className="text-xs mt-3 mb-2">Due Today/ Urgent:</span>
            <TaskList tasks={dueToday} />
            
            <span className="text-xs mb-2 mt-3">Upcoming:</span>
            <TaskList tasks={notCompletedTasks} />

            <span className="text-xs mb-2 mt-3">Completed:</span>
            <TaskList tasks={completedTasks} />
        </div>
      </div>
    </div>
  );
}

export default App;