package com.mz.storeapp.data.utils

import kotlinx.coroutines.flow.Flow

interface  NetworkMonitor
{
   val isOnline: Flow<Boolean>
}