var keystone = require('keystone');
var Types = keystone.Field.Types;

/**
 * User Model
 * ==========
 */

var Challenge = new keystone.List('Challenge');

Challenge.add({
	name: { type: Types.Name, required: true, index: true }
	
}, 'Permissions', {
	isAdmin: { type: Boolean, label: 'Can access Keystone', index: true }
});

// Provide access to Keystone
Challenge.schema.virtual('canAccessKeystone').get(function() {
	return this.isAdmin;
});


/**
 * Registration
 */

Challenge.defaultColumns = 'name, isAdmin';
Challenge.register();
