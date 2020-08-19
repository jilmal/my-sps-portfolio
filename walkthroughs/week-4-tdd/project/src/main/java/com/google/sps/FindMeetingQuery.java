// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        int dayStart = TimeRange.START_OF_DAY;
        int dayEnd = TimeRange.END_OF_DAY;
        long eventLength = request.getDuration();
        // make a new event var to not affect the var passed in to the method when removing irrelevant givenEvents!
        Collection<Event> givenEvents = events;
        TimeRange wholeDay = TimeRange.WHOLE_DAY;
        Collection<String> attendees = request.getAttendees();
        List<TimeRange> availableTimes = new ArrayList<TimeRange>();

        // if requested event is longer than the whole day return an empty availability list
        if (eventLength > wholeDay.duration()) {
            return availableTimes;
        }

        // if requested event or attendees are empty or null return that the whole day is available
        if (eventLength == 0 || givenEvents == null || attendees == null || givenEvents.isEmpty() || attendees.isEmpty()) {
            availableTimes.add(wholeDay);
            return availableTimes; 
        }

        //store unavailable times
        List<TimeRange> unavailableTimes = new ArrayList<TimeRange>();  
        for (Event event: givenEvents) {
            unavailableTimes.add(event.getWhen());
        }

        // ignore other givenEvents attendees are not part of
        Collection<String> eventAttendees = new ArrayList<String>();
        boolean conflict = false;
        for (Event event : givenEvents) {
            eventAttendees = event.getAttendees();

            // check if current event has a conflict with an attendee
            for (String attendee : attendees) {
                if (eventAttendees.contains(attendee)) {
                    conflict = true;
                }
            }

            // if current event does not have a conflict
            if (!conflict) {
                // edge case: if irrelevant event is the only event then return availbilty as whole day
                if (givenEvents.size() <= 1) {
                    availableTimes.add(wholeDay);
                    return availableTimes;
                }
                givenEvents.remove(event);
            }
            
            conflict = false;
        }

        // scan for potential open slots
        TimeRange potentialSlot;
        int scanStart = dayStart;
        int scanEnd;
        int i = 0;
        while(i < unavailableTimes.size()) {
            scanEnd = unavailableTimes.get(i).start();
            potentialSlot = TimeRange.fromStartEnd(scanStart, scanEnd, false);
            scanStart = unavailableTimes.get(i).end();
            
            // if found slot is the length needed then add to available times
            if (potentialSlot.duration() >= eventLength ) {
                availableTimes.add(potentialSlot); 
            }

            i++;
        }

        //check final slot
        Collections.sort(unavailableTimes, TimeRange.ORDER_BY_END);  
        TimeRange latest = unavailableTimes.get(unavailableTimes.size() - 1);      
        potentialSlot = TimeRange.fromStartEnd(latest.end(), dayEnd, true);
        if (potentialSlot.duration() >= eventLength ) {
            availableTimes.add(potentialSlot); 
        }
        
        return availableTimes;
    }
}
