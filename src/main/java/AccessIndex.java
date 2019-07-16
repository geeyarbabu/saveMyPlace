import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Iterator;

public class AccessIndex implements RequestHandler<Integer, String> {

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    DynamoDB dynamoDB = new DynamoDB(client);

    Table table = dynamoDB.getTable("placesOfInterest");
    Index index = table.getIndex("citiesSuggested");

    public String handleRequest(Integer integer, Context context) {

        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("cityOfLocation = :v_city")
                .withValueMap(new ValueMap()
                        .withString(":v_city", "dublin"));

        ItemCollection<QueryOutcome> items = index.query(spec);
        Iterator<Item> iter = items.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next().toJSONPretty());
        }
        return "Finished Display";
    }
}
