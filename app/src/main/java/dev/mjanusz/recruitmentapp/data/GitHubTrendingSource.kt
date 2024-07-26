package dev.mjanusz.recruitmentapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import dev.mjanusz.recruitmentapp.data.local.AppDatabase
import dev.mjanusz.recruitmentapp.data.local.dao.FavouritesDao
import dev.mjanusz.recruitmentapp.data.local.dao.LanguageDao
import dev.mjanusz.recruitmentapp.data.local.dao.RepositoriesDao
import dev.mjanusz.recruitmentapp.data.local.model.FavouriteRepoEntity
import dev.mjanusz.recruitmentapp.data.local.model.LanguageEntity
import dev.mjanusz.recruitmentapp.data.local.model.toEntities
import dev.mjanusz.recruitmentapp.data.local.model.toEntity
import dev.mjanusz.recruitmentapp.data.remote.GithubTrendingApi
import dev.mjanusz.recruitmentapp.data.remote.TrendingDateRange
import dev.mjanusz.recruitmentapp.data.remote.model.toLanguageList
import dev.mjanusz.recruitmentapp.ui.model.RepositoryLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubTrendingSource @Inject constructor(
    private val githubTrendingApi: GithubTrendingApi,
    private val repositoriesDao: RepositoriesDao,
    private val languagesDao: LanguageDao,
    private val favouritesDao: FavouritesDao,
    private val appDatabase: AppDatabase
) {

    suspend fun fetchAndUpdateTrendingRepos(
        language: RepositoryLanguage,
        since: TrendingDateRange = TrendingDateRange.DAILY
    ) = withContext(Dispatchers.IO) {
        val repos = githubTrendingApi.getTrendingRepos(
            language = language.urlPath,
            range = since
        ).toEntities()
        appDatabase.withTransaction {
            repositoriesDao.deleteAll()
            repositoriesDao.insertRepositories(repos)
        }
    }


    suspend fun fetchAndUpdateLanguages() =
        withContext(Dispatchers.IO) {
            val languages = githubTrendingApi.getLanguages().toLanguageList().map { it.toEntity() }
            appDatabase.withTransaction {
                languagesDao.deleteAll()
                languagesDao.insertLanguages(languages)
            }
        }


    suspend fun toggleFav(repoUrl: String, favourite: Boolean) =
        withContext(Dispatchers.IO) {
            if (favourite) {
                favouritesDao.insertFavourite(FavouriteRepoEntity(repoUrl))
            } else {
                favouritesDao.deleteFavourite(repoUrl)
            }
        }


    fun getTrendingRepos() = repositoriesDao.getRepositoriesWithFav()

    fun searchLanguages(query: String): Flow<PagingData<LanguageEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { languagesDao.searchLanguages(query) }
        ).flow
    }
}
