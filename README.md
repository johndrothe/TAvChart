A simple "Working Hours Diagram" AKA "Team Schedule Lineup" generator.

This version is a graphical desktop application using Java/Swing. 

# Background
I drew a daily working hours diagram in Draw IO for a client.
It was very when trying to figure out where schedules lined up and the time windows we 
could use to work together. We made great use of it when we were laying out the ceremonies 
we were going to use.

The following is the original Draw IO diagram.
<div style="width:600px">

![Working_Hours_no_names.drawio.png](docs/Working_Hours_no_names.drawio.png)
</div>


For example: A quick look at the diagram told us that the scrum master was going to have
access to the entire team only 1.5-2 hours per day. Our daily ceremonies were going to
have to be very lean to keep from chewing up that precious overlap time.

Considering the number of combined and near-shore teams we have, a tool that quickly 
generated this kind of diagram could be very valuable.

# Status
The current version is _workable_ but certainly not complete.

What works:

* Add, remove, reorder, and edit members.
* Undo / Redo
* Import / Export CSV
* Rudimentary PNG Export
* Time Zone rendering
* Normal and Lunch Hour(s) rendering
* Collaboration Zones
  * Calculation and display
    * Currently only displays the largest zone
      * Largest number of team members
      * Largest number of hours
    * Calculates all zones that
      * Start at one shift change and end at the next
* Displays daylight savings time transition dates for all included zones. 

<div style="width:600px">

![swing_working_hours_0-1-0](docs/swing_working_hours_0-1-0.png)
</div>

# Wishlist
Many (if not all) of the following items are not implemented.

* Context locations for Undo / Redo
  * i.e. "Go to where the change was"
* Canvas / Display
  * Complete handling for "Working Hours" that span days.
    * The text isn't right yet
  * Collaboration Zones (A.K.A. "Shifts")
    * Allow the user to choose which zones to display
    * Calculate zones for contiguous time periods that have the same team members.
      * These zones would overlap two or more existing zones. 
  * Center the canvas on a user-selected time
    * now it's always 0->24->0 UTC
  * Mouse handlers for changing the center canvas time
    * For teams that aren't working (entirely) in north america
  * Mouse handlers for changing a member's working hours
* Table Editing
  * Improved zone selector with search/sort
  * Proper spreadsheet-esque copy/paste
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
  * Daugherty Holidays
  * "Bank Holidays"
  * Holidays for the selected client (?)
