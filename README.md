<h1 align="center">KData</h1>
<div align="center">
  <strong>Data validator annotation processor</strong>
</div>
<div align="center">
  A simple framework for validating kotlin data class
</div>

<br />

<div align="center">
  <!-- Maintenance -->
  <a href="https://GitHub.com/Ibaykoc/Kdata/graphs/commit-activity">
    <img src="https://img.shields.io/badge/Maintained%3F-yes-green.svg"
      alt="Maintenance" />
  </a>
  <!-- Release version -->
  <a href="https://GitHub.com/Ibaykoc/KData/releases/">
    <img src="https://img.shields.io/github/release/Ibaykoc/KData.svg"
      alt="Release version" />
  </a>
  <!-- All downloads -->
    <img src="https://img.shields.io/github/downloads/Ibaykoc/KData/total.svg"
      alt="All downloads" />
</div>

------------

## The problem\...
Imagine you have **raw data** from API response like this:
```kotlin
data class LoginResponse (
    val status: Boolean?,
    val first_name: String?,
    val last_name: String?,
    val last_login: String?
)
```
But you only need some of the data for your **ui data** for example just `first_name`  and `last_name`, and you want all of the data to be **non nullable**, so your **ui data** would look like this:
```kotlin
data class LoginUiData (
    val firstName: String,
    val lastName: String
)
```
And if one of the needed field is **null** you dont want to use the data at all, 
then to validate it you probably have something like this:
```kotlin
fun validateLoginResponse(response: LoginResponse): LoginUiData? {
  if (response.first_name != && response.last_name != null)
    return LoginUiData(
      firstName = response.first_name!!,
      lastName = response.last_name!!
    )
  else
    return null
}
```
That would work, but imagine if you have a lot of data, then you have to write a lot of nullability check as well.

------------


## KData do this for you\...
With `@KData` you don't need to write all of the validation, because `@KData` will do all of that for you.

#### First
- Add `@KData` annotation to your *raw* data class,
- Pass the name that will be the name of the valid data class to `validatedClassName` parameter

```kotlin
// Your newly created valid data class will be named "LoginUiData"
@KData(validatedClassName = "LoginUiData") 
data class LoginResponse (
    val status: Boolean?,
    val first_name: String?,
    val last_name: String?,
    val last_login: String?
)
```
#### Second
- Tell `@Kdata` which field to validate by using `@KData.Field` annotation
- Pass the name that will be the name of the field in the validated data class to `validatedFieldName` parameter

```kotlin
// "first_name" Field in the validated data class will be named "firstName" 
// "last_name" Field in the validated data class will be named "lastName" 
@KData(validatedClassName = "LoginUiData") 
data class LoginResponse (
  val status: Boolean?,
  @KData.Field(validatedFieldName = "firstName")
  val first_name: String?,
  @KData.Field(validatedFieldName = "lastName")
  val last_name: String?,
  val last_login: String?
)
```
Then rebuild your project.
#### Thats it!

To validate *raw data* you just need to call **`.validate()`** on your *raw data* instance
```kotlin
...
val loginResponse = LoginResponse(
  status = true,
  first_name = "Jane",
  last_name = "Doe",
  last_login = "01-01-01 00:00"
)

println(loginResponse.validate())
...
```
output:
```sh
$ LoginUiData(firstName=Jane, lastName=Doe)
```

#### What if one of the needed field is **null**?
```kotlin
...
val loginResponse = LoginResponse(
  status = true,
  first_name = null, //one of the needed field is null
  last_name = "Doe",
  last_login = "01-01-01 00:00"
)

println(loginResponse.validate()) //.validate() will return null
...
```
output:
```sh
$ null
```

------------



## Implementation
Add this code to your build.gradle in project level:
```
allprojects {
  repositories {
    ...
      maven { url 'https://jitpack.io' }
    }
}
```
Then add this two dependencies to your module:
```
  apply plugin: 'kotlin-kapt'
  ...
  implementation 'com.github.ibaykoc.KData:kdataannotation:v0.1.0-alpha'
  kapt 'com.github.ibaykoc.KData:kdataprocessor:v0.1.0-alpha''
```
PS: If you have any suggestions or bugs, please create issues or Pull Requests.

**Enjoy!**
