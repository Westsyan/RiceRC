package filters

import javax.inject.Inject

import play.api.http.DefaultHttpFilters

import play.filters.gzip.GzipFilter



class Filters @Inject()(login: LoginFilter, gzipFilter: GzipFilter) extends DefaultHttpFilters(login,gzipFilter)