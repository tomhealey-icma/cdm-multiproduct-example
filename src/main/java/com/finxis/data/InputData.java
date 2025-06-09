package com.finxis.data;

import com.finxis.models.*;

import java.util.List;

public class InputData {

  public IrsOtcModel setIrsOctData(){

    IrsOtcModel irs = new IrsOtcModel();
    FixedRateLeg l1 = new FixedRateLeg();
    l1.payReceive = "PAY";
    l1.effectiveDate = "2025-05-20T00:00:00.000+00:00";
    l1.maturityDate = "2030-05-20T00:00:00.000+00:00";
    l1.interestRate = "4.5";
    l1.notional = "5000000";
    l1.payFreq = "Y";
    l1.currency = "USD";

    FloatingRateLeg l2 = new FloatingRateLeg();
    l2.payReceive = "RECEIVE";
    l2.effectiveDate = "2025-05-20T00:00:00.000+00:00";
    l2.maturityDate = "2030-05-20T00:00:00.000+00:00";
    l2.notional = "5000000";
    l2.payFreq = "Y";
    l2.currency = "USD";
    l2.index = "USD_SOFR_OIS_COMPOUND";
    l2.indexFreq = "3";

    irs.fixedRateLegList = List.of(l1);
    irs.floatingRateLegList = List.of(l2);

    irs.instrumentName = "IRS USD_SOFR_OIS_COMPOUND 2030-05-20";


    return irs;
  }

  public TradeModel setIrsOtcTradeData(){

    TradeModel tradeModel = new TradeModel();
    tradeModel.firmId = "DEALER";
    tradeModel.price = "104.5";
    tradeModel.priceCurrency= "USD";
    tradeModel.quantity = "5000000.00";
    tradeModel.counterparty1Id = "BuyerId";
    tradeModel.counterparty2Id = "SellerId";
    tradeModel.settlementDate = "2025-05-20T00:00:00.000+00:00";
    tradeModel.tradeDate = "2025-05-18T00:00:00.000+00:00";


    return tradeModel;

  }

  public FXOptionModel setFXOptionData(){

    FXOptionModel fxOptionModel = new FXOptionModel();

    fxOptionModel.underLyingAsset = "EUR/USD";
    fxOptionModel.callPut = "CALL";
    fxOptionModel.modelType = "BLACK";
    fxOptionModel.effectiveDate = "2025-05-20T00:00:00.000+00:00";
    fxOptionModel.expirationDate = "2025-08-20T00:00:00.000+00:00";
    fxOptionModel.deliveryDate = "2025-08-22T00:00:00.000+00:00";
    fxOptionModel.notionalCurrency = "USD";
    fxOptionModel.strike = "1.2";
    fxOptionModel.style = "EUROPEAN";
    fxOptionModel.volitility = "10";
    fxOptionModel.currency1 = "USD";
    fxOptionModel.currency2 = "JPY";
    fxOptionModel.instrumentName = fxOptionModel.currency1+fxOptionModel.currency2 + fxOptionModel.callPut+fxOptionModel.strike;

    return fxOptionModel;
  }

  public TradeModel setFXOptionTradeData(){

    TradeModel tradeModel = new TradeModel();
    tradeModel.firmId = "DEALER";
    tradeModel.price = "2.55";
    tradeModel.priceCurrency= "USD";
    tradeModel.quantity = "5000000.00";
    tradeModel.counterparty1Id = "BuyerId";
    tradeModel.counterparty2Id = "SellerId";
    tradeModel.settlementDate = "2025-05-20T00:00:00.000+00:00";
    tradeModel.tradeDate = "2025-05-18T00:00:00.000+00:00";

    return tradeModel;

  }

  public FXCashModel setFXCashData(){

    FXCashModel fxCashModel = new FXCashModel();
    fxCashModel.currency1 = "USD";
    fxCashModel.currency2 = "JPY";
    fxCashModel.valueDate = "2025-05-20T00:00:00.000+00:00";
    fxCashModel.instrumentName = fxCashModel.currency1+fxCashModel.currency2;

    return fxCashModel;

  }

  public TradeModel setFXCashTradeData(){

    TradeModel tradeModel = new TradeModel();
    tradeModel.firmId = "DEALER";
    tradeModel.price = "145.50";
    tradeModel.priceCurrency= "USD";
    tradeModel.quantity = "5000000.00";
    tradeModel.counterparty1Id = "BuyerId";
    tradeModel.counterparty2Id = "SellerId";
    tradeModel.settlementDate = "2025-05-20T00:00:00.000+00:00";
    tradeModel.tradeDate = "2025-05-18T00:00:00.000+00:00";

    return tradeModel;

  }
  public BondModel setBondData(){

    BondModel bondModel = new BondModel();

    bondModel.effectiveDate = "2020-04-09T00:00:00.000+00:00";
    bondModel.maturityDate = "2026-04-09T00:00:00.000+00:00";
    bondModel.couponRate = "2.375";
    bondModel.country = "D";
    bondModel.instrumentName = "25VG - LLOYDS 2.375% 09 Apr 2026";
    bondModel.currency = "EUR";
    bondModel.isin = "XS2151069775";
    bondModel.issuer = "Lloyds";


    return bondModel;

  }

  public BondModel setUSTreasuryBondData(){

    BondModel bondModel = new BondModel();

    bondModel.effectiveDate = "2016-02-15T00:00:00.000+00:00";
    bondModel.maturityDate = "2046-02-15T00:00:00.000+00:00";
    bondModel.couponRate = "2.5";
    bondModel.country = "USD";
    bondModel.instrumentName = "UST 2.5 2/15/46";
    bondModel.currency = "USD";
    bondModel.isin = "US912810RQ31";
    bondModel.paymentFrequency = "SA";
    bondModel.dayCountMethod = "ModifiedFollowing";
    bondModel.dayCountFraction = "30/360";
    bondModel.issuer = "US Treasury";



    return bondModel;

  }

  public BondFutureModel setBondFutureData(){

    BondFutureModel bondFutureModel = new BondFutureModel();
    bondFutureModel.instrumentName = "TN MAR5 Future";
    bondFutureModel.marketid = "CME";
    bondFutureModel.side = "Buy";
    bondFutureModel.tradeDate = "2025-02-07T10:00:00.000+00:00";
    bondFutureModel.underlyingIsin = "DE000F1C2NG8";
    bondFutureModel.price = "158.10000000";
    bondFutureModel.currency = "EUR";
    bondFutureModel.traderName = "CDMCXTRD018";
    bondFutureModel.nominalQuantity = "12.00000000";
    bondFutureModel.maturityDate = "2025-06-08T00:00:00.000+00:00";

    return bondFutureModel;

  }


}
