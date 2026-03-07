import type { TechniqueCount } from './technique-count'

export type Roll = {
  id: number 
  lengthMinutes: number
  partner: string
  numRounds: number
  subs: TechniqueCount[]
  taps: TechniqueCount[]
}