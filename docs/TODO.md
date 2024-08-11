# Features Wishlist

* Coherent logging and error reporting
* Move border and center hour to Team
* User-specified canvas size
  * Specified as
    * pixel
    * inch & DPI
    * cm & "DPI"
  * Canvas scrolling
  * Canvas Zoom
  * Export at full canvas size
* Refactor "Team" into "Document"
  * Name, Border/Center Hour, Members, Canvas Size, etc.
* Save/load Team (Document) in JSON 
* Color icons for the toolbar / menu bar 
* Context locations for Undo / Redo
  * i.e. "Go to where the change was"
* Canvas / Display
  * Complete handling for "Working Hours" that span days.
    * The text isn't right yet
  * Collaboration Zones
    * Allow the user to choose which zones to display
    * Calculate zones for contiguous time periods that have the same team members.
      * These zones would overlap two or more of the zones that are currently being calculated.
* Table Editing
  * Improved zone selector with search/sort
* Data Entry
  * Multi-select and import employees folks from Salesforce
  * Pull their "home" region / time zone from Salesforce
  * Pull their normal working hours from Outlook (if easily available)
  * Multi-select any number of additional time zones
* Output separate diagrams for each section of a year
  * Include start/end dates
  * Automatically start a new diagram any time daylight savings time starts in one of the selected zones.
* Include arbitrary time indicator lines (shown as vertical lines below)
  * Can be used to mark "Overlap Zones" or anything else necessary.
* Export formats
  * Excel
  * SVG
  * Full Featured PNG
  * Draw IO (?)
* As a bonus, consider also generating a combined calendar with holidays for each region / country
  * Company Holidays
  * "Bank Holidays"
  * Holidays for the selected client (?)
