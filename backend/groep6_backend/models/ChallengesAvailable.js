var keystone = require('keystone'),
    Types = keystone.Field.Types;

var ChallengeAvailable = new keystone.List('ChallengeAvailable');

ChallengeAvailable.add({
    title: { type: String, required: false },
	category: { type: Types.Select, options: 'student, single, family', default: 'family' },
	image: { type: Types.CloudinaryImage }
});


ChallengeAvailable.defaultColumns = 'title, category, image'
ChallengeAvailable.register();