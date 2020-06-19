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

package com.google.sps.data;
import com.google.appengine.api.datastore.Entity;

/** A comment in comment section. */
public final class Comment {

  private final long id;
  private final String input;
  private final long timestamp;

  public Comment(long id, String input, long timestamp) {
    this.id = id;
    this.input = input;
    this.timestamp = timestamp;
  }

  public static Comment fromEntity(Entity entity) {
    long id = entity.getKey().getId();
    String input = (String) entity.getProperty("input");
    long timestamp = (long) entity.getProperty("timestamp");
    return new Comment(id, input, timestamp);
  }

  @Override
  public String toString() {
      return "ID: " + id + ", Input: " + input + ", Timestamp: " + timestamp;
  }
}