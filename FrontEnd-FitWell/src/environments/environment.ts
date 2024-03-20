// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  apiUrl: 'http://localhost:4201',
  authUrl: 'http://localhost:4201/auth',
  userUrl: 'http://localhost:4201/users/profile',
  exerciseUrl: 'http://localhost:4201/exercise',
  setUrl: 'http://localhost:4201/set',
  workoutUrl: 'http://localhost:4201/workout',
  cardWorkoutUrl: 'http://localhost:4201/card_workout',
  statsUrl: 'http://localhost:4201/stat'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
