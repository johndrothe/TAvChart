I drew a daily working hours diagram in Draw IO for a client.
It was very when trying to figure out where schedules lined up and the time windows we 
could use to work together. We made great use of it when we were laying out the ceremonies 
we were going to use.

<div style="width:600px">

![Working_Hours_no_names.drawio.png](docs/Working_Hours_no_names.drawio.png)
</div>


For example: A quick look at the diagram told us that the scrum master was going to have
access to the entire team only 1.5-2 hours per day. Our daily ceremonies were going to
have to be very lean to keep from chewing up that precious overlap time.

Considering the number of combined and near-shore teams we have, a tool that quickly 
generated this kind of diagram could be very valuable.

* Could be built in nearly any tech stack.
* Data Entry
  * Optional CSV Import
  * Directly enter non-Daugherty folks
  * Multi-select Daugherty folks from Salesforce
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