import { useState } from "react";
import type { KeyboardEvent } from "react";
import { useTaskStore } from "@/stores/taskStore";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input"


export function AddTask() {
    const { createTask } = useTaskStore();

    const [ input, setInput ] = useState("");
    const [ error, setError ] = useState("");

    function handleEnterKeyPress<T = Element>(f: () => void){
        return handleKeyPress<T>(f, "Enter")
    }

    function handleKeyPress<T = Element>(f: () => void, key: string){
        return (e: KeyboardEvent<T>) => {
            if(e.key === key){
                f()
            }
        }
    }

    function useRegex(inp: string) {
        let regex = /^([A-Za-z]+(?: [A-Za-z]+)*)\s+due:\d\d-\d\d-\d\d\d\d\s+@\s+\d\d:\d\d/i;
        return regex.test(inp);
    }

    return (
        <div className="flex flex-col">
            <div className="flex flex-row justify-between"> 
                <p>Enter task in the format: task name due:dd-mm-YYYY @ hh:mm </p>
                {error !== '' && <p className="text-red-500">{error}</p>}
            </div>
            <Card className="p-1 flex justify-between flex-row items-center rounded-xs mt-1 mb-1 text-xs">
                <Input 
                    className={`w-full p-2 m-2 rounded-xs text-xs ${error !== '' ? 'border-red-500 active:border-red-500' : ''}`}
                    placeholder="Establish CORS setup due:04-09-2026 @ 10:00"
                    onKeyDown={handleEnterKeyPress(() => (console.log("enter pressed")))}
                    value={input} onChange={(e)=> setInput(e.currentTarget.value)}
                >
                </Input>
            </Card>
        </div>
    )
}   