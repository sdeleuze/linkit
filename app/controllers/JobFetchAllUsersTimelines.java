package controllers;

import helpers.TransactionCallback;
import helpers.TransactionCallbackWithoutResult;
import helpers.TransactionTemplate;
import java.util.List;
import models.Account;
import models.activity.StatusActivity;
import play.Logger;
import play.Play;
import play.db.jpa.NoTransaction;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Asynchronous fetch of user timelines on external providers (Google+, Twitter)
 * @author Sryl <cyril.lacote@gmail.com>
 */
// FIXME Job disabled
// @Every("1h")
@NoTransaction
public class JobFetchAllUsersTimelines extends Job {

    // Read-only template
    private TransactionTemplate txTemplate = new TransactionTemplate(true);
    
    @Override
    public void doJob() {        
        if (!"test".equals(Play.id)) {
            Logger.info("BEGIN fetch timelines");

            List<Long> accountsId = txTemplate.execute(new TransactionCallback() {
                public List<Long> doInTransaction() {
                    return Account.findAllIds();
                }
            });

            // Not read-only transaction
            txTemplate.setReadOnly(false);
            for (final Long id : accountsId) {

                try {
                    txTemplate.execute(new TransactionCallbackWithoutResult() {
                        public void doInTransaction() {
                            StatusActivity.fetchForAccount(id);
                        }
                    });
                } catch (Exception e) {
                    Logger.error("Exception while fetching account, skipped to next", e);
                }
            }
            Logger.info("END fetch timelines");
        }
    }
}
