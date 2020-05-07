/*
 * MinIO Java SDK for Amazon S3 Compatible Cloud Storage, (C) 2020 MinIO, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.minio;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(
    value = "BC",
    justification = "Valid casting of subclass instance in BucketArgs.")
@SuppressWarnings("unchecked")
public abstract class BucketArgs {
  private String name;
  private String region;

  public String name() {
    return name;
  }

  public String region() {
    return region;
  }

  BucketArgs(Builder<?> builder) {
    this.name = builder.name;
    this.region = builder.region;
    validate();
  }

  private void validate() {
    validateName();
    // TODO: validate region
  }

  private void validateName() {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("null or empty bucket name");
    }

    // Bucket names cannot be no less than 3 and no more than 63 characters long.
    if (name.length() < 3 || name.length() > 63) {
      throw new IllegalArgumentException(
          name + " : " + "bucket name must be at least 3 and no more than 63 characters long");
    }
    // Successive periods in bucket names are not allowed.
    if (name.contains("..")) {
      String msg =
          "bucket name cannot contain successive periods. For more information refer "
              + "http://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html";
      throw new IllegalArgumentException(name + " : " + msg);
    }
    // Bucket names should be dns compatible.
    if (!name.matches("^[a-z0-9][a-z0-9\\.\\-]+[a-z0-9]$")) {
      String msg =
          "bucket name does not follow Amazon S3 standards. For more information refer "
              + "http://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html";
      throw new IllegalArgumentException(name + " : " + msg);
    }
  }

  public static class Builder<T extends Builder<T>> {
    public String name;
    public String region;

    public T bucket(String name) {
      this.name = name;
      return (T) this;
    }

    public T region(String region) {
      this.region = region;
      return (T) this;
    }
  }
}
