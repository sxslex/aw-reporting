//Copyright 2012 Google Inc. All Rights Reserved.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package com.google.api.ads.adwords.awreporting.server.rest.reports;

import com.google.api.ads.adwords.awreporting.model.entities.Report;
import com.google.api.ads.adwords.awreporting.model.entities.ReportBase;
import com.google.api.ads.adwords.awreporting.model.entities.ReportPlaceholderFeedItem;
import com.google.api.ads.adwords.awreporting.server.rest.AbstractBaseResource;
import com.google.api.ads.adwords.awreporting.server.rest.RestServer;
import com.google.api.ads.adwords.awreporting.server.util.StorageHelper;

import org.restlet.representation.Representation;

import java.util.Date;
import java.util.List;

/**
 * Rest entry point generic for all Report Types
 */
public abstract class AbstractReportRest<ReportSub extends Report> extends AbstractBaseResource {

  private Class<ReportSub> classReportSub;
  
  private StorageHelper storageHelper = RestServer.getStorageHelper();

  protected AbstractReportRest(Class<ReportSub> classReportSub) {
    this.classReportSub = classReportSub;
  }

  public Representation getHandler() {
    String result = null;
    try {
      
      Long topAccountId = getParameterAsLong("topAccountId");
      Long accountId = getParameterAsLong("accountId");
      Long campaignId = getParameterAsLong("campaignId");
      Long adGroupId = getParameterAsLong("adGroupId");
      Long adId = getParameterAsLong("adId");
      Long adExtensionId = getParameterAsLong("adExtensionId");
      Long criterionId = getParameterAsLong("criterionId");
      Long feedId = getParameterAsLong("feedId");
      Long feedItemId = getParameterAsLong("feedItemId");

      Date dateStart = getParameterAsDate("dateStart");
      Date dateEnd = getParameterAsDate("dateEnd");

      Integer offset = getParameterAsInteger("offset");
      Integer limit = getParameterAsInteger("limit");

      // Defaults to DAY
      String dateRangeType;
      String dateRangeTypeString = getParameter("dateRangeType");
      if (dateRangeTypeString != null && dateRangeTypeString.equalsIgnoreCase(ReportBase.MONTH)) {
        dateRangeType = ReportBase.MONTH;
      } else {
        dateRangeType = ReportBase.DAY;
      }

      List<ReportSub> listReport = null;

      if (topAccountId != null &&
          accountId == null &&
          campaignId == null &&
          adGroupId == null && 
          adId == null &&
          adExtensionId == null &&
          criterionId == null &&
          feedId == null &&
          feedItemId == null) {
        listReport = storageHelper.getReport(classReportSub,
            Report.TOP_ACCOUNT_ID, topAccountId, dateRangeType, dateStart, dateEnd, offset, limit);
      }

      if (accountId != null) {
        listReport = storageHelper.getReport(classReportSub,
            Report.ACCOUNT_ID, accountId, dateRangeType, dateStart, dateEnd, offset, limit);
      }

      if (campaignId != null) {
        listReport = storageHelper.getReport(classReportSub,
            Report.CAMPAIGN_ID, campaignId, dateRangeType, dateStart, dateEnd, offset, limit);
      }

      if (adGroupId != null) {
        listReport = storageHelper.getReport(classReportSub,
            Report.ADGROUP_ID, adGroupId, dateRangeType, dateStart, dateEnd, offset, limit);
      }

      if (adId != null) {
        listReport = storageHelper.getReport(classReportSub,
            Report.AD_ID, adId, dateRangeType, dateStart, dateEnd, offset, limit);
      }

      if (adExtensionId != null) {
        listReport = storageHelper.getReport(classReportSub,
            Report.ADEXTENSION_ID, adExtensionId, dateRangeType, dateStart, dateEnd, offset, limit);
      }

      if (criterionId != null) {
        listReport = storageHelper.getReport(classReportSub,
            Report.KEYWORD_ID, criterionId, dateRangeType, dateStart, dateEnd, offset, limit);
      }

      if (feedId != null) {
        listReport = storageHelper.getReport(classReportSub,
            ReportPlaceholderFeedItem.FEED_ID, feedId, dateRangeType, dateStart, dateEnd, offset, limit);
      }

      if (feedItemId != null) {
        listReport = storageHelper.getReport(classReportSub,
            ReportPlaceholderFeedItem.FEED_ITEM_ID, feedItemId, dateRangeType, dateStart, dateEnd, offset, limit);
      }

      if (listReport != null) {
          result = gson.toJson(listReport);
      }
    } catch (Exception exception) {
      return handleException(exception);
    }

    addReadOnlyHeaders();
    return createJsonResult(result);
  }
}
