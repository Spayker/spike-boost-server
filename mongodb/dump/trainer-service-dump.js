/**
 * Creates pre-filled demo train
 */

print('trainer dump start');

db.trainers.update(
    { "_id": "demo" },
    { upsert: true }
);

print('trainer dump complete');