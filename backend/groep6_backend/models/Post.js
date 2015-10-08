var keystone = require('keystone'),
    Types = keystone.Field.Types;

var Challenge = new keystone.List('Challenge');

Challenge.add({
    title: { type: String, required: false },
});


Challenge.defaultColumns = 'title'
Challenge.register();