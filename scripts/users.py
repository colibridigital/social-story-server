from firebase import firebase
import argparse

FB_INSTANCE = 'https://sizzling-torch-6706.firebaseio.com'
USERS_ROOT = 'social-story/users'


def add_user(username, users, fb):
    print 'Adding user'
    new_user = {
        'username': username,
        'score': 0
    }
    fb.post(USERS_ROOT, new_user)


def main():

    print 'Loading users'
    fb = firebase.FirebaseApplication(FB_INSTANCE, None)
    users = fb.get(USERS_ROOT, None)
    print users
    
    parser = argparse.ArgumentParser(description='User management script.')
    parser.add_argument('-a',
                        dest='username',
                        help='Add a user')
    args = parser.parse_args()
    if args.username:
        add_user(args.username, users, fb)
    

if __name__ == '__main__':
    main()
