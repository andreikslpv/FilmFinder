package com.andreikslpv.filmfinder.data.datasource.api.imdb

object ImdbError {
    private val errorMap = mapOf(
        "Invalid_Api_Key" to "Invalid API Key",
        "User_Email_Not_Confirmed" to "Email not confirmed",
        "User_Suspended" to "Your account has been suspended.",
        "Invalid_Id" to "Invalid Id",
        "Invalid_Season_Number" to "Invalid SeasonNumber",
        "Season_Number_Not_Valid_For_Movie" to "SeasonNumber is not valid for Movie",
        "Season_Number_Grather(string existingSeasons)" to "SeasonNumber grather than existing seasons (existingSeasons)",
        "Server_Busy" to "Server busy",
        "Exception" to "Exception",
        "Your_Api_Key_Has_Expired" to "Your API Key has expired",
        "Maximum_Usage(int current, int total)" to "Maximum usage (current of total per day)",
        "Not_Founded_404" to "404 Not Founded Error",
        "VideoGame_Not_Valid" to "VideoGame is not valid",
        "Invalid_Request(string apiName)" to "Invalid request. See more information at ...",
        "TvEpisode_Type_Is_Not_Valid" to "TvEpisode type is not valid",
        "MusicVideo_Type_Is_Not_Valid" to "MusicVideo type is not valid",
        "Wikipedia_Not_Founded_In_Lang(string lang)" to "Wikipedia not founded in 'lang' language",
        "Bad_Request" to "Bad Request",
        "Adult" to "Adult is not valid",
        "Year_Empty" to "Year is empty",
        "Copyright_Not_Valid" to "Deleted for copyright",
        "YouTube_Invalid_VideoId" to "Invalid YouTube Video Id",
        "List_Is_Empty" to "List is empty",
        "AdvancedSearchWithoutFilter" to "It is mandatory to enter at least one filter."
    )

    fun getError(errorMessage: String?): Throwable {
        return try {
            Throwable(errorMap[errorMessage], null)
        }
        catch (e: Exception) {
            Throwable("Unknown error", null)
        }
    }
}