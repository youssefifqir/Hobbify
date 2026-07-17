INSERT INTO app_achievement (ref, name, description, type, threshold, icon_url) VALUES
  ('seed-ach-first-step', 'First Step', 'Complete your first step in any guide.', 'ONBOARDING', 1, '🌱'),
  ('seed-ach-getting-started', 'Getting Started', 'Complete 5 steps across your guides.', 'MILESTONE', 5, '⭐'),
  ('seed-ach-dedicated', 'Dedicated', 'Complete 25 steps across your guides.', 'MILESTONE', 25, '🏆'),
  ('seed-ach-explorer', 'Explorer', 'Start 3 different hobbies.', 'EXPLORER', 3, '🧭'),
  ('seed-ach-finisher', 'Finisher', 'Complete every step of a hobby guide.', 'MASTERY', 1, '🎓'),
  ('seed-ach-consistent', 'Consistent', 'Complete steps on 3 different days.', 'STREAK', 3, '🔥')
ON CONFLICT (ref) DO NOTHING;
