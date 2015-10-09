var keystone = require('keystone'),
    Types = keystone.Field.Types;

var ChallengePerUser = new keystone.List('ChallengePerUser');

ChallengePerUser.add({
    title: { type: String, required: false },
    category: { type: Types.Select, options: 'student, single, family', default: 'family' },
    image: { type: Types.CloudinaryImage },
    completed : { type: Types.Boolean, default: 'false' },
    user: { type: Types.Relationship, ref: 'User' }
});


ChallengePerUser.defaultColumns = 'title, category, image, completed, user'
ChallengePerUser.register();