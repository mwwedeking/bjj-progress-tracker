import type { Roll } from "./roll"

export type Session = {
  id: number
  date: string 
  time: string 
  isGi: boolean
  instructor: string
  currentBelt: string
  rolls: Roll[]
}