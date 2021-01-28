package com.epam.voucherifyintegrator.client;

import com.epam.voucherifyintegrator.job.UpdateLoyaltyProgramJob;
import io.voucherify.client.VoucherifyClient;
import io.voucherify.client.error.VoucherifyError;
import io.voucherify.client.model.campaign.CampaignType;
import io.voucherify.client.model.campaign.CreateCampaign;
import io.voucherify.client.model.campaign.UpdateCampaign;
import io.voucherify.client.model.campaign.response.CampaignResponse;
import io.voucherify.client.model.customer.response.CustomerCampaignLoyalty;
import io.voucherify.client.model.customer.response.CustomerResponse;
import io.voucherify.client.model.loyalties.CreateEarningRule;
import io.voucherify.client.model.loyalties.Loyalty;
import io.voucherify.client.model.loyalties.UpdateEarningRule;
import io.voucherify.client.model.loyalties.response.ListEarningRulesResponse;
import io.voucherify.client.model.order.response.GetOrderResponse;
import io.voucherify.client.model.order.response.ListOrdersResponse;
import io.voucherify.client.model.voucher.*;
import io.voucherify.client.model.voucher.response.VoucherResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

public class MyVoucherifyClient {

    private final String secretKey;
    private final String applicationId;
    private VoucherifyClient client;

    Logger logger = LogManager.getLogger(MyVoucherifyClient.class);

    private static final String COVID_CAMPAIGN = "COVID Loyalty program";
    private static final String ORDER_PAID_EVENT = "order.paid";
    private static final Integer VOUCHERS_COUNT = 1;

    public MyVoucherifyClient(String secretKey, String applicationId) {
        this.secretKey = secretKey;
        this.applicationId = applicationId;
        setUpClient();
    }

    private void setUpClient() {
        try {
            client = new VoucherifyClient.Builder()
                    .setAppId(applicationId)
                    .setClientSecretKey(secretKey)
                    .build();
        } catch (VoucherifyError e) {
            logger.error("Some problem with connection to Voucherify occurred!", e);
        }
    }

    public Double getYesterdayAverageOrderSum() {
        try {
            ListOrdersResponse listOrders = client.orders().list();
            if (listOrders.getOrders() != null && !listOrders.getOrders().isEmpty()) {
                BigDecimal sum = BigDecimal.valueOf(0);
                long count = 0;
                for (GetOrderResponse order : listOrders.getOrders()) {
                    if (isDateWasYesterday(order.getCreatedAt())) {
                        sum = sum.add(BigDecimal.valueOf(order.getAmount()));
                        count++;
                    }
                }
                return sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.CEILING)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING)
                        .doubleValue();
            } else {
                return 0d;
            }
        } catch (Exception ex) {
            logger.error("Can't get yesterday average order sum", ex);
            return 0d;
        }
    }

    public Integer getSumOfPoints() {
        try {
            ListOrdersResponse listOrders = client.orders().list();
            CampaignResponse campaign = client.campaigns().get(COVID_CAMPAIGN);
            if (listOrders.getOrders() != null && !listOrders.getOrders().isEmpty()) {
                Integer sum = 0;
                for (GetOrderResponse order : listOrders.getOrders()) {
                    if (campaign.getStartDate().before(order.getCreatedAt())) {
                        Optional<CustomerResponse> customer = getCustomerById(order.getCustomer().getId());
                        if (customer.isPresent()) {
                            CustomerCampaignLoyalty loyalty = customer.get().getLoyalty().getCampaigns().get(COVID_CAMPAIGN);
                            if (loyalty != null) {
                                sum += loyalty.getPoints();
                            }
                        }
                    }
                }
                return sum;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            logger.error("Can't get sum of points earned by COVID loyalty program", ex);
            return 0;
        }
    }

    private boolean isDateWasYesterday(Date date) {
        DateTime dateTime = new DateTime(date);
        Date now = new Date();
        DateTime yesterday = new DateTime(now);
        return dateTime.getYear() == yesterday.getYear()
                && dateTime.getMonthOfYear() == yesterday.getMonthOfYear()
                && dateTime.getDayOfMonth() == (yesterday.getDayOfMonth() - 1);
    }

    public Optional<CustomerResponse> getCustomerBySourceId(String sourceId) {
        try {
            return client.customers().list()
                    .getCustomers()
                    .stream()
                    .filter(val -> val.getSourceId().equalsIgnoreCase(sourceId))
                    .findFirst();
        } catch (Exception ex) {
            logger.error("Can't get customer by sourceId = {}", sourceId, ex);
            return Optional.empty();
        }
    }

    public Optional<CustomerResponse> getCustomerById(String id) {
        try {
            return Optional.of(client.customers().get(id));
        } catch (Exception ex) {
            logger.error("Can't get customer by id = {}", id, ex);
            return Optional.empty();
        }
    }

    public Optional<Integer> getPointsByCustomer(CustomerResponse customer) {
        try {
            Integer sum = customer.getLoyalty()
                    .getCampaigns()
                    .get(COVID_CAMPAIGN)
                    .getPoints();
            return Optional.of(sum);
        } catch (Exception ex) {
            logger.error("Can't get points by customer", ex);
            return Optional.empty();
        }
    }

    public Optional<CampaignResponse> getCampaignByName(String name) {
        try {
            CampaignResponse response = client.campaigns().get(name);
            if (response != null) {
                return Optional.of(response);
            } else {
                return Optional.empty();
            }
        } catch (Exception ex) {
            logger.error("Can't get campaign by name = {}", name, ex);
            return Optional.empty();
        }
    }

    public void updateLoyaltyProgramProperties(CampaignResponse campaign, Integer points) {
        try {
            ListEarningRulesResponse response = client.loyalties().listEarningRules(campaign.getId(), null);
            if (response != null && response.getTotal() > 0) {
                String ruleId = response.getData().get(0).getId();
                Loyalty loyalty = Loyalty.builder()
                        .points(points < 0
                                ? points * (-1)
                                : points)
                        .build();
                UpdateEarningRule updateEarningRule = UpdateEarningRule.builder()
                        .loyalty(loyalty)
                        .build();
                client.loyalties().updateEarningRule(campaign.getId(), ruleId, updateEarningRule);
            }
        } catch (Exception ex) {
            logger.error("Can't update loyalty program properties", ex);
        }
    }

    public void createLoyaltyProgram(Integer points) {
        try {
            Date now = new Date();
            Voucher voucher = createVoucher();
            CreateCampaign campaign = CreateCampaign.builder()
                    .name(COVID_CAMPAIGN)
                    .startDate(now)
                    .vouchersCount(VOUCHERS_COUNT)
                    .type(CampaignType.AUTO_UPDATE)
                    .voucher(voucher)
                    .build();
            CampaignResponse campaignResponse = client.loyalties().create(campaign);
            Loyalty loyalty = Loyalty.builder()
                    .points(points < 0
                            ? points * (-1)
                            : points)
                    .build();
            CreateEarningRule earningRule = CreateEarningRule.builder()
                    .event(ORDER_PAID_EVENT)
                    .loyalty(loyalty)
                    .build();
            client.loyalties().createEarningRules(campaignResponse.getId(), Collections.singletonList(earningRule));
        } catch (Exception ex) {
            logger.error("Can't create loyalty program", ex);
        }
    }

    private Voucher createVoucher() {
        return Voucher.builder()
                .type(VoucherType.LOYALTY_CARD)
                .active(true)
                .isReferralCode(false)
                .startDate(new Date())
                .campaign(COVID_CAMPAIGN)
                .build();
    }
}
