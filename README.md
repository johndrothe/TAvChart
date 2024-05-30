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

You can add, remove, and edit members.

<div style="width:600px">
![swing_working_hours_0-0-1.drawio.png](docs/swing_working_hours_0-0-1.drawio.png)
</div>

# Wishlist
Many (if not all) of the following items are not implemented.

* Display
  * Complete handling for "Working Hours" that span days.
    * The text isn't right yet
  * Collaboration Zones (A.K.A. "Shifts")
    * Create zones for contiguous time periods that have the same team members.
    * Allow the user to choose which zones to display
  * Lunch Hour display
  * Center the canvas on a user-selected time
    * now it's always 0->24/0
  * Mouse handlers for changing the center canvas time
    * For teams that aren't working (entirely) in north america
  * Mouse handlers for changing a member's working hours
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
  * SVG / PNG
  * Draw IO (?)
* As a bonus, consider also generating a combined calendar with holidays for each region / country
  * Daugherty Holidays
  * "Bank Holidays"
  * Holidays for the selected client (?)
