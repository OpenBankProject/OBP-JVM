package com.tesobe.obp.demo.north;

import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.util.tbd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.route.RouteOverview;

import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.ipAddress;
import static spark.Spark.port;

class Rest
{
  Rest(Connector c, String ip, int port)
  {
    connector = c;

    ipAddress(ip);
    port(port);

    get("/obp/demo/bank/:bank", this::getBank);
    get("/obp/demo/bank/:bank/account/:account", this::getAccount);
    get("/obp/demo/bank/:bank/accounts", this::getAccounts);
    get("/obp/demo/bank/:bank/account/:account/transaction/:transaction",
      this::getTransaction);
    get("/obp/demo/bank/:bank/account/:account/transactions",
      this::getTransactions);
    get("/obp/demo/banks", this::getBanks);

    RouteOverview.enableRouteOverview("/obp/demo");

    awaitInitialization();
  }

  private Object getAccount(Request request, Response response)
  {
    throw new tbd();
  }

  private Object getAccounts(Request request, Response response)
  {
    throw new tbd();
  }

  private Object getBank(Request request, Response response)
  {
    throw new tbd();
  }

  private Object getBanks(Request request, Response response)
  {
    throw new tbd();
  }

  private Object getTransaction(Request request, Response response)
  {
    throw new tbd();
  }

  private Object getTransactions(Request request, Response response)
  {
    throw new tbd();
  }

  private final Connector connector;
  final static Logger log = LoggerFactory.getLogger(Rest.class);
}
