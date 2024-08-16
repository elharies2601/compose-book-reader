package id.elharies.composereader.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.elharies.composereader.repository.book.BookRepository
import id.elharies.composereader.repository.book.BookRepositoryImpl
import id.elharies.composereader.repository.firebase.FirebaseRepository
import id.elharies.composereader.repository.firebase.FirebaseRepositoryImpl

@Module(includes = [AppModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideFirebaseRepository(repositoryImpl: FirebaseRepositoryImpl): FirebaseRepository

    @Binds
    abstract fun provideBookRepository(repositoryImpl: BookRepositoryImpl): BookRepository
}