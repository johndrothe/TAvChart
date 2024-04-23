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


# Current Wishlist
Many (if not all) of the following items are not implemented.

* Data Entry
  * Optional CSV Import
  * Directly enter team members, their locations, and their positions
  * Multi-select and import employees folks from Salesforce
  * Pull their "home" region / time zone from Salesforce
  * Assume working hours of 08:00 - 17:00 in their "home" time zone
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
